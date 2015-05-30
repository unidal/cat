package org.unidal.cat.event.view;

import org.unidal.cat.event.report.ReportPage;
import org.unidal.web.mvc.Page;

public class NavigationBar {
   public Page[] getVisiblePages() {
      return new Page[] {
   
      ReportPage.EVENT

		};
   }
}
