package org.unidal.cat.nav;

import javax.servlet.http.HttpServletRequest;

import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Named;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.Validator;

@Named(type = Validator.class, value = "^nav.store")
public class NavStoreInterceptor extends ContainerHolder implements Validator<ActionContext<?>> {
	@Override
	public void validate(ActionContext<?> ctx) throws Exception {
		HttpServletRequest req = ctx.getHttpServletRequest();
		NavStore nav = new NavStore(req);

		ctx.setAttribute(NavStore.ID, nav);
		req.setAttribute(NavStore.ID, nav);
	}
}
