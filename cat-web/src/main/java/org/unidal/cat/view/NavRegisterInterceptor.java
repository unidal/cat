package org.unidal.cat.view;

import javax.servlet.http.HttpServletRequest;

import org.unidal.cat.nav.NavRegister;
import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Named;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.Validator;

@Named(type = Validator.class, value = "^nav.register")
public class NavRegisterInterceptor extends ContainerHolder implements Validator<ActionContext<?>> {
	@Override
	public void validate(ActionContext<?> ctx) throws Exception {
		HttpServletRequest req = ctx.getHttpServletRequest();
		NavRegister register = new NavRegister(req);

		ctx.setAttribute("nav", register);
		req.setAttribute("nav", register);
	}
}
