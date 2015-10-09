package com.t1t.digipolis.apim.scim;

import com.unboundid.scim.data.Entry;
import com.unboundid.scim.data.Name;
import com.unboundid.scim.data.UserResource;
import com.unboundid.scim.schema.CoreSchema;
import com.unboundid.scim.sdk.SCIMException;
import com.unboundid.scim.sdk.SCIMService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by michallispashidis on 8/10/15.
 */
public class APIMIntegrationTest {
    private SCIMService client;

    /**
     * Runs the client test program.
     *
     * @param args  The arguments used to initialize the test program.
     * @throws Exception  If the test program fails.
     */
    public static void main(final String[] args) throws Exception
    {
        final String host = "idp.t1t.be:9443/wso2/scim";
        final String userName = "admin";
        final String password = "admin";

        final SCIMService client = new SCIMService(URI.create("https://" + host), userName, password);

        final APIMIntegrationTest clientTest = new APIMIntegrationTest(client);
        clientTest.testGetUser();
        //clientTest.testCreateUser();
        //clientTest.testEditUser();
        //clientTest.testDeleteUser();

        System.out.println("All tests completed successfully");
        System.exit(0);
    }



    /**
     * Create a new instance of this test client.
     *
     * @param client  The SCIM wink client to be used by this test client.
     */
    public APIMIntegrationTest(final SCIMService client)
    {
        this.client = client;
    }



    /**
     * Tests creation of a new user via XML.
     *
     * @throws Exception if error creating a new user.
     */
    public void testCreateUser() throws Exception
    {
        createUser();
    }



    /**
     * Tests updating a new user via XML.
     *
     * @throws Exception if error editing a new user.
     */
    public void testEditUser() throws Exception
    {
        String changedAttributeValue = "ASSERT_DISPLAY_NAME_CHANGED";

        final UserResource user = createUser();
        user.setDisplayName(changedAttributeValue);
        final UserResource userFromResponse =
                client.getUserEndpoint().update(user);
        assertEquals(userFromResponse.getDisplayName(), changedAttributeValue);
    }



    /**
     * Tests updating a new user via XML.
     *
     * @throws Exception if error editing a new user.
     */
    public void testDeleteUser() throws Exception
    {
        final UserResource user = createUser();
        client.getUserEndpoint().delete(user);
    }


    /**
     * Tests user retrieval via XML.
     *
     * @throws Exception if error fetching the User.
     */
    public void testGetUser() throws Exception
    {
        final UserResource user = createUser();
        final UserResource userFromResponse =
                client.getUserEndpoint().get(user.getId());
        assertNotNull(userFromResponse);
    }



    /**
     * Creates a new SCIM User.
     *
     * @return The newly created SCIM User.
     * @throws SCIMException If error creating the user.
     */
    private UserResource createUser()
            throws SCIMException
    {
        return client.getUserEndpoint().create(getTemplateUser());
    }



    /**
     * Throw a runtime exception if the provided condition is not true.
     * @param condition  The condition which is expected to be true.
     */
    private static void assertTrue(final boolean condition)
    {
        if (!condition)
        {
            throw new RuntimeException("assertTrue assertion failed");
        }
    }



    /**
     * Throw a runtime exception if the actual and expected objects are not equal.
     * @param o1  The actual value.
     * @param o2  The expected value.
     */
    private static void assertEquals(final Object o1, final Object o2)
    {
        if (!o1.equals(o2))
        {
            throw new RuntimeException("assertEquals assertion failed");
        }
    }



    /**
     * Throw a runtime exception if the provided value is {@code null}.
     * @param o  The object which is not expected to be null.
     */
    private static void assertNotNull(final Object o)
    {
        if (o == null)
        {
            throw new RuntimeException("assertNotNull assertion failed");
        }
    }



    /**
     * Creates a POJO representing the canonical SCIM User 'Babs'.
     *
     * @return The templated User.
     */
    private static UserResource getTemplateUser()
    {
        // create new user
        final UserResource user = new UserResource(CoreSchema.USER_DESCRIPTOR);

        // make the user unique enough
        user.setUserName("testusermichallis" + new Date().getTime());
        user.setExternalId(user.getUserName());
        user.setDisplayName("Michallis Pashidis Tester");
        final Name name = new Name("Michallis Pashidis Tester", "Pashidis", null,
                "Michallis", null, null);
        user.setName(name);

        final Collection<Entry<String>> emails = new ArrayList<Entry<String>>(1);
        final Entry<String> email =
                new Entry<String>("michallis@trust1team.com", null, false);
        emails.add(email);
        user.setEmails(emails);

        return user;
    }
}
