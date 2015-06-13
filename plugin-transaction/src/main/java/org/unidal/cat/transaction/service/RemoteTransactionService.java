package org.unidal.cat.transaction.service;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.dianping.cat.report.service.BaseRemoteModelService;
import org.unidal.cat.transaction.analyzer.TransactionAnalyzer;
import org.unidal.cat.transaction.model.entity.TransactionReport;
import org.unidal.cat.transaction.model.transform.DefaultSaxParser;

public class RemoteTransactionService extends BaseRemoteModelService<TransactionReport> {
	public RemoteTransactionService() {
		super(TransactionAnalyzer.ID);
	}

	@Override
	protected TransactionReport buildModel(String xml) throws SAXException, IOException {
		return DefaultSaxParser.parse(xml);
	}
}
