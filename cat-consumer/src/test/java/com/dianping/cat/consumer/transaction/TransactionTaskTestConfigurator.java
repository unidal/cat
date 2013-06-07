package com.dianping.cat.consumer.transaction;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dainping.cat.consumer.core.dal.TaskPayloadDao;
import com.dianping.cat.consumer.transaction.TransactionTaskTest.MockTaskPayloadDao;

public class TransactionTaskTestConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(TaskPayloadDao.class, MockTaskPayloadDao.class));
		
		return all;
	}

	@Override
	protected Class<?> getTestClass() {
		return TransactionTaskTest.class;
	}
	
	public static void main(String[] args) {
	   generatePlexusComponentsXmlFile(new TransactionTaskTestConfigurator());
   }
}
