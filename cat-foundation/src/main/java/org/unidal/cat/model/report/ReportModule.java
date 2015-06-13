package org.unidal.cat.model.report;

import org.unidal.web.mvc.AbstractModule;
import org.unidal.web.mvc.annotation.ModuleMeta;
import org.unidal.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "report", defaultInboundAction = "model", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

org.unidal.cat.model.report.page.Handler.class
})
public class ReportModule extends AbstractModule {

}
