package grp2.fitness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import java.util.Optional;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Optional.ofNullable(IdentityManager.getDefaultIdentityManager()).ifPresent((identityManager) -> {
            if (identityManager.isUserSignedIn()) {
                IdentityManager.getDefaultIdentityManager().signOut();
            }
        });

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                AuthUIConfiguration loginScreenConfig = new AuthUIConfiguration.Builder()
                        .userPools(true)
                        .logoResId(R.drawable.proteinpowder)
                        .backgroundColor(R.color.colorPrimary)
                        .isBackgroundColorFullScreen(true)
                        .build();

                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(LoginActivity.this, SignInUI.class);
                signin.login(LoginActivity.this, NavigationActivity.class)
                        .authUIConfiguration(loginScreenConfig).execute();
            }
        }).execute();
    }

    public void loginHome(View view) {
        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
        finish();
    }
}