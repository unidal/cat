package com.dianping.cat.report.page.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.lookup.util.StringUtils;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.dianping.cat.Cat;
import com.dianping.cat.consumer.transaction.TransactionStatisticsComputer;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForNameTotal;
import com.dianping.cat.consumer.transaction.model.entity.Machine;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseVisitor;
import com.dianping.cat.consumer.transaction.model.transform.DefaultMaker;
import com.dianping.cat.consumer.transaction.model.transform.VisitorChain;
import com.dianping.cat.report.ReportConstants;
import com.dianping.cat.report.ReportPage;
import com.dianping.cat.report.ReportRepository;
import com.dianping.cat.report.graph.GraphBuilder;
import com.dianping.cat.report.model.ModelRequest;
import com.dianping.cat.report.page.NormalizePayload;
import com.dianping.cat.report.page.PieChart;
import com.dianping.cat.report.page.PieChart.Item;
import com.dianping.cat.report.page.transaction.GraphPayload.AverageTimePayload;
import com.dianping.cat.report.page.transaction.GraphPayload.DurationPayload;
import com.dianping.cat.report.page.transaction.GraphPayload.FailurePayload;
import com.dianping.cat.report.page.transaction.GraphPayload.HitPayload;
import com.dianping.cat.report.service.ReportService;
import com.google.gson.Gson;

public class Handler implements PageHandler<Context> {
	@Inject
	private GraphBuilder m_builder;

	@Inject
	private HistoryGraphs m_historyGraph;

	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private XmlViewer m_xmlViewer;

	@Inject
	private ReportService m_reportService;

	@Inject
	private NormalizePayload m_normalizePayload;

	@Inject
	private ReportRepository<TransactionReport> m_repository;

	private TransactionStatisticsComputer m_computer = new TransactionStatisticsComputer();

	private String buildPieChart(TransactionReport report) {
		final List<Item> items = new ArrayList<Item>();

		report.accept(new BaseVisitor() {
			@Override
			public void visitName(TransactionName name) {
				items.add(new Item(name.getId(), name.getTotalCount()));
			}
		});

		PieChart chart = new PieChart();

		chart.setItems(items);
		return new Gson().toJson(chart);
	}

	private void calculateTps(Payload payload, TransactionReport report) {
		try {
			if (payload != null && report != null) {
				boolean isCurrent = payload.getPeriod().isCurrent();
				String ip = payload.getIpAddress();
				Machine machine = report.getMachines().get(ip);
				if (machine == null) {
					return;
				}
				for (TransactionType transType : machine.getTypes().values()) {
					long totalCount = transType.getTotalCount();
					double tps = 0;
					if (isCurrent) {
						double seconds = (System.currentTimeMillis() - payload.getCurrentDate()) / (double) 1000;
						tps = totalCount / seconds;
					} else {
						double time = (report.getEndTime().getTime() - report.getStartTime().getTime()) / (double) 1000;
						tps = totalCount / (double) time;
					}
					transType.setTps(tps);
					for (TransactionName transName : transType.getNames().values()) {
						long totalNameCount = transName.getTotalCount();
						double nameTps = 0;
						if (isCurrent) {
							double seconds = (System.currentTimeMillis() - payload.getCurrentDate()) / (double) 1000;
							nameTps = totalNameCount / seconds;
						} else {
							double time = (report.getEndTime().getTime() - report.getStartTime().getTime()) / (double) 1000;
							nameTps = totalNameCount / (double) time;
						}
						transName.setTps(nameTps);
						transName.setTotalPercent((double) totalNameCount / totalCount);
					}
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
	}

	private TransactionName getHourlyGraphs(Payload payload) {
		String domain = payload.getDomain();
		String type = payload.getType();
		String name = payload.getName();
		String ip = payload.getIpAddress();
		String ipAddress = payload.getIpAddress();
		String date = String.valueOf(payload.getDate());

		if (name == null || name.length() == 0) {
			name = ReportConstants.ALL;
		}

		ModelRequest request = new ModelRequest(domain, payload.getPeriod()) //
		      .setReportName("transaction") //
		      .setProperty(ReportConstants.PROPERTY_DATE, date) //
		      .setProperty(ReportConstants.PROPERTY_IP, ipAddress) //
		      .setProperty(ReportConstants.PROPERTY_TYPE, type) //
		      .setProperty(ReportConstants.PROPERTY_NAME, name)//
		      .setProperty(ReportConstants.PROPERTY_GRAPH, "true");

		TransactionReport report = m_repository.queryHouylyReport(request);
		TransactionType t = report.findMachine(ip).findType(type);

		if (t != null) {
			TransactionName n = t.findName(name);

			if (n != null) {
				n.accept(m_computer);

				return n;
			}
		}

		return null;
	}

	private TransactionReport getHourlyReport(Payload payload) {
		String domain = payload.getDomain();
		String date = String.valueOf(payload.getDate());
		String ipAddress = payload.getIpAddress();
		ModelRequest request = new ModelRequest(domain, payload.getPeriod()) //
		      .setReportName("transaction") //
		      .setProperty(ReportConstants.PROPERTY_DATE, date) //
		      .setProperty(ReportConstants.PROPERTY_IP, ipAddress) //
		      .setProperty(ReportConstants.PROPERTY_TYPE, payload.getType()) //
		      .setProperty(ReportConstants.PROPERTY_PATTERN, payload.getQueryName()) //
		      .setProperty(ReportConstants.PROPERTY_SORT_BY, payload.getSortBy());

		TransactionReport report = m_repository.queryHouylyReport(request);

		return report;
	}

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "t")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "t")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();

		normalize(model, payload);

		switch (payload.getAction()) {
		case HOURLY_REPORT:
			showHourlyReport(model, payload);
			break;
		case HISTORY_REPORT:
			showSummarizeReport(model, payload);
			break;
		case HISTORY_GRAPH:
			m_historyGraph.buildTrendGraph(model, payload);
			break;
		case GRAPHS:
			showHourlyGraphs(model, payload);

			break;
		}

		if (payload.isXml()) {
			m_xmlViewer.view(ctx, model);
		} else {
			m_jspViewer.view(ctx, model);
		}
	}

	private void normalize(Model model, Payload payload) {
		model.setPage(ReportPage.TRANSACTION);
		m_normalizePayload.normalize(model, payload);

		if (StringUtils.isEmpty(payload.getQueryName())) {
			payload.setQueryName(null);
		}
		if (StringUtils.isEmpty(payload.getType())) {
			payload.setType(null);
		}

		String queryName = payload.getQueryName();
		if (queryName != null) {
			model.setQueryName(queryName);
		}
	}

	private void showHourlyGraphs(Model model, Payload payload) {
		TransactionName name = getHourlyGraphs(payload);

		if (name != null) {
			String graph1 = m_builder.build(new DurationPayload("Duration Distribution", "Duration (ms)", "Count", name));
			String graph2 = m_builder.build(new HitPayload("Hits Over Time", "Time (min)", "Count", name));
			String graph3 = m_builder.build(new AverageTimePayload("Average Duration Over Time", "Time (min)",
			      "Average Duration (ms)", name));
			String graph4 = m_builder.build(new FailurePayload("Failures Over Time", "Time (min)", "Count", name));

			model.setGraph1(graph1);
			model.setGraph2(graph2);
			model.setGraph3(graph3);
			model.setGraph4(graph4);
		}
	}

	private void showHourlyReport(Model model, Payload payload) {
		try {
			TransactionReport report = getHourlyReport(payload);

			if (report != null) {
				report.accept(m_computer);
			}

			if (payload.getType() != null && payload.getType().length() > 0) {
				model.setPieChart(buildPieChart(report));

				DefaultMaker maker = new DefaultMaker();

				report.accept(new VisitorChain(maker, new TransactionAggregatorForNameTotal()));
				report = maker.getTransactionReport();
			}

			model.setReport(report);
		} catch (Throwable e) {
			Cat.logError(e);
			model.setException(e);
		}
	}

	private void showSummarizeReport(Model model, Payload payload) {
		String domain = model.getDomain();
		Date start = payload.getHistoryStartDate();
		Date end = payload.getHistoryEndDate();
		TransactionReport transactionReport = m_reportService.queryTransactionReport(domain, start, end);

		calculateTps(payload, transactionReport);
		model.setReport(transactionReport);
	}

	public enum DetailOrder {
		TYPE, NAME, TOTAL_COUNT, FAILURE_COUNT, MIN, MAX, SUM, SUM2
	}

	public enum SummaryOrder {
		TYPE, TOTAL_COUNT, FAILURE_COUNT, MIN, MAX, SUM, SUM2
	}
}
