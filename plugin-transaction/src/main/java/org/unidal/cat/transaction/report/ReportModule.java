package org.unidal.cat.transaction.report;

import org.unidal.web.mvc.AbstractModule;
import org.unidal.web.mvc.annotation.ModuleMeta;
import org.unidal.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "report", defaultInboundAction = "transaction", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

org.unidal.cat.transaction.report.page.Handler.class
})
public class ReportModule extends AbstractModule {

}
