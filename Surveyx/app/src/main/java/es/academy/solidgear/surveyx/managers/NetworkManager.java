package es.academy.solidgear.surveyx.managers;

import es.academy.solidgear.surveyx.BuildConfig;
import es.academy.solidgear.surveyx.api.APIService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static NetworkManager instance = null;

    private final APIService api;

    private NetworkManager() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl(BuildConfig.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.api = client.create(APIService.class);
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public APIService getAPI() {
        return api;
    }

}
