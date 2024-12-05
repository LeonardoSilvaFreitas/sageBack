package sage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.io.IOException;
import java.io.InputStream;
/**
 * Classe responsável por inicializar o Firebase no início da aplicação.
 */
@ApplicationScoped
public class FirebaseInitialize {

    /**
     * Método chamado no início da aplicação para inicializar o Firebase.
     *
     * @param ev O evento de inicialização do Quarkus.
     */
    void onStart(@Observes StartupEvent ev) {
        try {
            // Verificar se a instância do Firebase já foi inicializada
            if (FirebaseApp.getApps().isEmpty()) {  // Verifica se há instâncias existentes
                InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket("appteste-6aa53.appspot.com")
                        .build();

                FirebaseApp.initializeApp(options); // Inicializa apenas se não houver instâncias
                System.out.println("Firebase App initialized successfully.");
            } else {
                System.out.println("Firebase App already initialized.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

