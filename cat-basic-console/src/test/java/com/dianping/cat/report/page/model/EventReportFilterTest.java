package com.dianping.cat.report.page.model;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.helper.Files;

import com.dianping.cat.consumer.event.EventReportXmlBuilder;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.event.model.transform.DefaultSaxParser;

public class EventReportFilterTest {
	@Test
	public void test() throws Exception {
		String source = Files.forIO().readFrom(getClass().getResourceAsStream("event.xml"), "utf-8");
		EventReport report = DefaultSaxParser.parse(source);

		EventReportXmlBuilder f1 = new EventReportXmlBuilder(null, null, null);
		String expected1 = Files.forIO().readFrom(getClass().getResourceAsStream("event-type.xml"), "utf-8");

		Assert.assertEquals(expected1.replaceAll("\r", ""), f1.buildXml(report).replaceAll("\r", ""));

		EventReportXmlBuilder f2 = new EventReportXmlBuilder("URL", null, null);
		String expected2 = Files.forIO().readFrom(getClass().getResourceAsStream("event-name.xml"), "utf-8");

		Assert.assertEquals(expected2.replaceAll("\r", ""), f2.buildXml(report).replaceAll("\r", ""));
	}
}
