package org.apache.atlas;

import com.sun.jersey.api.client.WebResource;
import org.apache.atlas.typesystem.IInstance;
import org.apache.atlas.typesystem.types.IDataType;
import org.apache.commons.configuration.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TalendAtlasClient extends AtlasClient {

    public TalendAtlasClient(String baseUrl) {
        super(baseUrl);
    }

    public TalendAtlasClient(String baseUrl, UserGroupInformation ugi, String doAsUser) {
        super(baseUrl, ugi, doAsUser);
    }

    public TalendAtlasClient(UserGroupInformation ugi, String doAsUser, String... baseUrls) {
        super(ugi, doAsUser, baseUrls);
    }

    TalendAtlasClient(WebResource service, Configuration configuration) {
        super(service, configuration);
    }

    /**
     * Deletes all the entities of the given types
     * @param types
     */
    public void deleteEntitiesByType(String... types) throws AtlasServiceException {
        for (String type : types) {
            List<String> guids = this.listEntities(type);
            if (guids.size() > 0) {
                String[] guidAsArray = guids.toArray(new String[guids.size()]);
                this.deleteEntities(guidAsArray);
            }
        }
    }

    /**
     * TODO return the real object, not the json representation
     * @return the full list of types available
     */
    public Collection<String> getAllTypes() throws AtlasServiceException {
        //This can be really big but for development/debug reasons it could be really useful
        Collection<String> types = new ArrayList<>();
        List<String> existingTypesDef = this.listTypes();
        for (String existingTypeDef: existingTypesDef) {
            String type = this.getType(existingTypeDef);
            types.add(type);
        }
        return types;
    }

    /**
     * Checks if the instance is a correct representation of the datatype,
     * this is useful to avoid calling the server to validate instances (refs)
     * @param dataType
     * @param instance
     * @throws AtlasException
     */
    public boolean checkTypes(IDataType dataType, IInstance instance) throws AtlasException {
        //TODO this one has lower priority but it would be a blessing to haves
        return false;
    }
}
