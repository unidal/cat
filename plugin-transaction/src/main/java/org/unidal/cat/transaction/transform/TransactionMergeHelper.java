package org.unidal.cat.transaction.transform;

import org.unidal.lookup.util.StringUtils;

import com.dianping.cat.Constants;
import org.unidal.cat.transaction.model.entity.TransactionReport;

public class TransactionMergeHelper {

	public TransactionReport mergeAllMachines(TransactionReport report, String ipAddress) {
		if (StringUtils.isEmpty(ipAddress) || Constants.ALL.equalsIgnoreCase(ipAddress)) {
			AllMachineMerger all = new AllMachineMerger();

			all.visitTransactionReport(report);
			report = all.getReport();
		}
		return report;
	}

	public TransactionReport mergeAllNames(TransactionReport report, String allName) {
		if (StringUtils.isEmpty(allName) || Constants.ALL.equalsIgnoreCase(allName)) {
			AllNameMerger all = new AllNameMerger();

			all.visitTransactionReport(report);
			report = all.getReport();
		}
		return report;
	}

	public TransactionReport mergeAllNames(TransactionReport report, String ipAddress, String allName) {
		TransactionReport temp = mergeAllMachines(report, ipAddress);

		return mergeAllNames(temp, allName);
	}

}
