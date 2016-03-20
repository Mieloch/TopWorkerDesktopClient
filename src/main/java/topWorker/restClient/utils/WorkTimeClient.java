package topWorker.restClient.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import topWorker.restClient.Message;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by echomil on 20.03.16.
 */
public class WorkTimeClient {

    private WebTarget webTarget;
    private ObjectMapper mapper;
    public WorkTimeClient(String target, String username, String password){

        Client client = ClientBuilder.newClient(new ClientConfig().register(HttpAuthenticationFeature.basic(username, password)));
        webTarget = client.target(target);

        mapper = new ObjectMapper();

    }
    public boolean postMessage(Message message){
        String m = null;
        try {
            m = mapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);
        try {
           Response response = invocationBuilder.post(Entity.json(m), Response.class);
            return(response.getStatus() == 200);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
