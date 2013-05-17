package com.dianping.cat.consumer.transaction;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.helper.Files;
import org.xml.sax.SAXException;

import com.dianping.cat.consumer.transaction.model.IFilter;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForMachine;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForName;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForType;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByMachine;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByName;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByNamePattern;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByType;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByTypePattern;
import com.dianping.cat.consumer.transaction.model.TransactionFilterWithGraph;
import com.dianping.cat.consumer.transaction.model.TransactionSorterByName;
import com.dianping.cat.consumer.transaction.model.TransactionSorterByType;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.transform.DefaultMaker;
import com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser;
import com.dianping.cat.consumer.transaction.model.transform.DefaultXmlBuilder;
import com.dianping.cat.consumer.transaction.model.transform.VisitorChain;

public class TransactionReportTest {
	private void checkReport(String source, IFilter... filters) throws IOException, SAXException {
		TransactionReport report = loadReport("transaction.xml");
		TransactionReport expectedReport = loadReport(source);

		// build xml directly
		StringBuilder sb = new StringBuilder(8192);
		DefaultXmlBuilder builder = new DefaultXmlBuilder(false, sb);

		report.accept(new VisitorChain(builder, filters));

		Assert.assertEquals(expectedReport.toString(), sb.toString());

		// build object
		DefaultMaker maker = new DefaultMaker();

		report.accept(new VisitorChain(maker, filters));

		Assert.assertEquals(expectedReport.toString(), maker.getTransactionReport().toString());
	}

	private TransactionReport loadReport(String resource) throws IOException, SAXException {
		String xml = Files.forIO().readFrom(getClass().getResourceAsStream(resource), "utf-8");
		TransactionReport report = DefaultSaxParser.parse(xml);

		return report;
	}

	@Test
	public void testNameByAllIpAndType() throws IOException, SAXException {
		checkReport("transaction-name-by-all-ip-and-type.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByType("URL"), //
		      new TransactionFilterWithGraph(false), //
		      new TransactionSorterByName("total"));
	}

	@Test
	public void testNameByAllIpAndTypeAndPattern() throws IOException, SAXException {
		checkReport("transaction-name-by-all-ip-and-type-and-pattern.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByType("URL"), //
		      new TransactionFilterByNamePattern("t|p"), //
		      new TransactionFilterWithGraph(false), //
		      new TransactionSorterByName("total"));
	}

	@Test
	public void testNameByIpAndType() throws IOException, SAXException {
		checkReport("transaction-name-by-ip-and-type.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByType("URL"), //
		      new TransactionFilterWithGraph(false), //
		      new TransactionSorterByName("total"));
	}

	@Test
	public void testNameByIpAndTypeAndPattern() throws IOException, SAXException {
		checkReport("transaction-name-by-ip-and-type-and-pattern.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByType("URL"), //
		      new TransactionFilterByNamePattern("t|e|p"), //
		      new TransactionFilterWithGraph(false), //
		      new TransactionSorterByName("total"));
	}

	@Test
	public void testNameGraphByAllIpAndTypeAndAllName() throws IOException, SAXException {
		checkReport("transaction-name-graph-by-all-ip-and-type-and-all-name.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByType("URL"), //
		      new TransactionAggregatorForName());
	}

	@Test
	public void testNameGraphByAllIpAndTypeAndName() throws IOException, SAXException {
		checkReport("transaction-name-graph-by-all-ip-and-type-and-name.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByType("URL"), //
		      new TransactionFilterByName("t"));
	}

	@Test
	public void testNameGraphByIpAndTypeAndAllName() throws IOException, SAXException {
		checkReport("transaction-name-graph-by-ip-and-type-and-all-name.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByType("URL"), //
		      new TransactionAggregatorForName());
	}

	@Test
	public void testNameGraphByIpAndTypeAndName() throws IOException, SAXException {
		checkReport("transaction-name-graph-by-ip-and-type-and-name.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByType("URL"), //
		      new TransactionFilterByName("t"));
	}

	@Test
	public void testTypeByAllIp() throws IOException, SAXException {
		checkReport("transaction-type-by-all-ip.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByName(null), //
		      new TransactionSorterByType("type"));
	}

	@Test
	public void testTypeByAllIpAndPattern() throws IOException, SAXException {
		checkReport("transaction-type-by-all-ip-and-pattern.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByTypePattern("S|s|M"), //
		      new TransactionFilterByName(null), //
		      new TransactionSorterByType("type"));
	}

	@Test
	public void testTypeByIp() throws IOException, SAXException {
		checkReport("transaction-type-by-ip.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByName(null), //
		      new TransactionSorterByType("type"));
	}

	@Test
	public void testTypeByIpAndPattern() throws IOException, SAXException {
		checkReport("transaction-type-by-ip-and-pattern.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByTypePattern("s|M"), //
		      new TransactionFilterByName(null), //
		      new TransactionSorterByType("type"));
	}

	@Test
	public void testTypeGraphByAllIpAndAllType() throws IOException, SAXException {
		checkReport("transaction-type-graph-by-all-ip-and-all-type.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionAggregatorForType(), //
		      new TransactionAggregatorForName());

	}

	@Test
	public void testTypeGraphByAllIpAndType() throws IOException, SAXException {
		checkReport("transaction-type-graph-by-all-ip-and-type.xml", //
		      new TransactionAggregatorForMachine(), //
		      new TransactionFilterByType("URL"), //
		      new TransactionAggregatorForName());
	}

	@Test
	public void testTypeGraphByIpAndAllType() throws IOException, SAXException {
		checkReport("transaction-type-graph-by-ip-and-all-type.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionAggregatorForType(), //
		      new TransactionAggregatorForName());
	}

	@Test
	public void testTypeGraphByIpAndType() throws IOException, SAXException {
		checkReport("transaction-type-graph-by-ip-and-type.xml", //
		      new TransactionFilterByMachine("192.168.10.125"), //
		      new TransactionFilterByType("URL"), //
		      new TransactionAggregatorForName());
	}

	@Test
	public void testXmlParseAndBuild() throws Exception {
		String source = Files.forIO().readFrom(getClass().getResourceAsStream("transaction.xml"), "utf-8");
		TransactionReport report = DefaultSaxParser.parse(source);
		String xml = new DefaultXmlBuilder().buildXml(report);
		String expected = source;

		Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), xml.replace("\r", ""));
	}
}
