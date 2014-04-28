/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.couchbase;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.CouchbaseClient;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's Couchbase
 * Repositories.
 * 
 * @author Michael Nitschinger
 * @since 1.1.0
 * @see CouchbaseProperties
 * @see EnableCouchbaseRepositories
 */
@Configuration
@ConditionalOnClass({ CouchbaseClient.class, CouchbaseTemplate.class })
@EnableConfigurationProperties(CouchbaseProperties.class)
public class CouchbaseAutoConfiguration {

	@Autowired
	private CouchbaseProperties properties;

	@PreDestroy
	public void close() throws URISyntaxException, IOException {
		this.properties.closeClient();
	}

	@Bean
	@ConditionalOnMissingBean(CouchbaseClient.class)
	public CouchbaseClient couchbaseClient() throws URISyntaxException, IOException {
		return this.properties.createClient();
	}

	@Bean
	@ConditionalOnMissingBean(CouchbaseTemplate.class)
	public CouchbaseTemplate couchbaseTemplate(CouchbaseClient couchbaseClient) {
		return new CouchbaseTemplate(couchbaseClient);
	}

}