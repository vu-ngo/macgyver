/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.service.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.macgyver.neorx.rest.NeoRxClient;
import io.macgyver.test.MacGyverIntegrationTest;

public class Neo4jConfigLoaderTest extends MacGyverIntegrationTest {

	@Autowired
	CompositeConfigLoader cl;
	
	@Autowired
	NeoRxClient neo4j;
	
	@Test
	public void testIt() {
	
			neo4j.execCypher("match (c:ServiceConfig) where c.serviceName=~'junit.*' detach delete c");
		
	}
}
