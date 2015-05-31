package org.unidal.cat.matrix.view;

import org.unidal.cat.matrix.report.ReportPage;
import org.unidal.web.mvc.Page;

public class NavigationBar {
   public Page[] getVisiblePages() {
      return new Page[] {
   
      ReportPage.MATRIX

		};
   }
}
