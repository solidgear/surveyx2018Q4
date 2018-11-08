package es.academy.solidgear.surveyx.models;

public class LoginModel {

    private class LoginData {
        private String token;
        private String username;

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return this.token;
        }
    }

    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private LoginData results[];

    public LoginModel(String token) {
        this.results[0].setToken(token);
    }

    public String getToken() {
        return results[0].getToken();
    }

}
