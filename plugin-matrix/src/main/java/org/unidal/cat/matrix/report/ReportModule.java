package org.unidal.cat.matrix.report;

import org.unidal.web.mvc.AbstractModule;
import org.unidal.web.mvc.annotation.ModuleMeta;
import org.unidal.web.mvc.annotation.ModulePagesMeta;

@ModuleMeta(name = "report", defaultInboundAction = "matrix", defaultTransition = "default", defaultErrorAction = "default")
@ModulePagesMeta({

org.unidal.cat.matrix.report.page.Handler.class
})
public class ReportModule extends AbstractModule {

}