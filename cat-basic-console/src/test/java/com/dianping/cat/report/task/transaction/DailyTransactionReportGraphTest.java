package com.dianping.cat.report.task.transaction;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.webres.helper.Files;

import com.dainping.cat.consumer.core.dal.Dailygraph;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser;

public class DailyTransactionReportGraphTest {

	@Test
	public void test() throws Exception {
		String oldXml = Files.forIO()
		      .readFrom(getClass().getResourceAsStream("TransactionReportDailyGraph.xml"), "utf-8");
		TransactionReport report1 = DefaultSaxParser.parse(oldXml);

		DailyTransactionGraphCreator creator = new DailyTransactionGraphCreator();

		List<Dailygraph> graphs = creator.buildDailygraph(report1);

		Assert.assertEquals(3, graphs.size());
	}
}
