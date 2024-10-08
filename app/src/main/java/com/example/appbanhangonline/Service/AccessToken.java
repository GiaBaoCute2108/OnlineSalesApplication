package com.example.appbanhangonline.Service;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {
    private final static String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";

    public String getAccessToken() {
        try {
            String jsonString = "{\"type\": \"service_account\",\n" +
                    "  \"project_id\": \"appbanhang-ec403\",\n" +
                    "  \"private_key_id\": \"84f602d7cc805747043cbde6cb90f75440edf2e4\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCx4GtccUC6Vivp\\n07j8CxNTt/q2+vBSxXeMpxgq8xWsMfEr/wTzlWz5bhLjb+MEX23VM9dYVtF5I28W\\nbn0HR/32hqc7NFgIe42ZeCgvVcrmV6Vz1uwGoPM8FTRuetrA2jZAGpZHl/i2gGEA\\nECUMfbH0NZ/KVwtXWLdthvmMzT9TVWLf/minNKDe+DCWt9hW3MFKiDQjuAFpJyNm\\n5OXTXs6voEmlsVarUHdDhG0etuZhdw5NoPNShdRs194gKqahCC+ZL9QRmQxiz9+a\\nUvm5VdBbrSVQx7MEuaUfugbH1IXDPMjORApLmZRcudW1U+YKMyZiHURDjdKSLwt8\\nj0Y10j3pAgMBAAECggEAVvNBB/tEjNG6vZ6Feo9fbzksEW4xNATbH6I+qiARpA71\\nuW/716Zfkr4/9XWoH2cWpXWl+sTVuMWdc/WmoHyMpInGDabUsbDT0wVtf1YMzPRj\\nKHETB1p+DAzBiI/9hvAkPMnCVFTxeyTeRc2ejrCS4LGl3p8WKChx4dRtAGsEXZM7\\nEJLRopzNlqJBK18kdG3TyLWCFgtM5Z3oVLzyFynlCcq/G7pRlqUBDfcgfTXHfYsY\\n/KOhvkadqt8RViNJM/9n13Ymy/WVjOWvi7/8JtcaLTNqdHh6C1lC/cO5FLUIT3Es\\nS9oIej32I7h0JQInndYcUoorpkNVLtdZiZYzkiHv0QKBgQDYazHh3aMXxPlW8wTU\\nIhCaPbflQ92zuxOXwr8mNpbMfg7j5EF2DSuFWacsUbRtawedhC0RpKepgdWoejbC\\nNGsCF4bmJYLy6vUx+G/6cRud1EqchtoJTi4PhGHH3pbskJjw5NiIrt/L/fgCXDq7\\nvmRwUGI0gSZ4FfykDXDCwPPdZwKBgQDSaKtk5ODWNAwmFZ3bjSjpFV6nUo+EG3+y\\nO40Ing9ZxxDEWXJUAA+5J1RlPLSyyn78obdnDMQmPUc34Eg3++dmSJ6/LIXVbcqp\\nJVvKQ1/wVDKfe/HUg6n+1xiEx/L8eW2yyxlztKW0y3I0SIXGJyGDRfCl07yClSgT\\nneqomJeoLwKBgQCvncTbsRvQhQGEYi3v6jiowR6CB6FXYJ6Uju7IFpImHMD+gtHv\\n9t3zFv5GQ4LzK2ABnSlpYMDBF1+GmK3ox9px1x+bkKMDdlBEHYLYqoIrgAKPpAo4\\ndN2Br/4cz26PRwMkr79aovVuNofTen4arXQZg9E9I/RQjB74DQQMtcDuTwKBgDg0\\nIWRIBw7wxDYAHDSjVkQ8oxYK5a8iMVAIvDmLDwqqM/5e7j0l7w1ERjTsrE/960HM\\naNMsA9E7GSUB0giyTzNxPoZOfElwlGvuQMCckqn9s7AKSaS8rtIV3sTJLEzomrbh\\nraDMpl7O4Wi53pH5regE7K0/C+HNufDTg/UZHZmdAoGBAL85+jbv87/r7w8MvgaY\\nLirhtwW9SMP77DHw6BsUhd+FIqoYqsgctU9BwJncCPSM54iO5u74sHBVd3/mggeJ\\nyLBgsen4MFWhoBpNCkmitVFk34RTrCUoYSzzrg6y8QS5/UGldqSmOz2XMWon8T9X\\nZKoHuPBspSK6/R7aJREzigPg\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-630i4@appbanhang-ec403.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"113832505884437539669\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-630i4%40appbanhang-ec403.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"}";

            InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(Lists.newArrayList(firebaseMessagingScope));
            googleCredentials.refresh();
            String accessToken = googleCredentials.getAccessToken().getTokenValue();
            Log.d("AccessToken", "Access Token: " + accessToken);
            return accessToken;
        } catch (IOException e) {
            Log.e("AccessTokenError", "Error getting access token: " + e.getMessage(), e);
            return null;
        }
    }

}