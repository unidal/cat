package org.unidal.cat.transaction.view;

import org.unidal.cat.transaction.report.ReportPage;
import org.unidal.web.mvc.Page;

public class NavigationBar {
   public Page[] getVisiblePages() {
      return new Page[] {
   
      ReportPage.TRANSACTION

		};
   }
}
