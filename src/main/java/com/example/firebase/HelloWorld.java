// src/main/java/com/example/firebase/HelloWorld.java
package com.example.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HelloWorld {

    private static final String SERVICE_ACCOUNT_KEY_PATH = "path/to/your/serviceAccountKey.json"; // IMPORTANT: Update this path!

    public static void main(String[] args) {
        try {
            // 1. Initialize Firebase Admin SDK
            // You need to download a service account key JSON file from your Firebase project settings.
            // Go to Project settings > Service accounts > Generate new private key.
            // Replace "path/to/your/serviceAccountKey.json" with the actual path to your downloaded file.
            FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_KEY_PATH);

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                // You can optionally set a database URL if you're using Realtime Database
                // .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com")
                .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Admin SDK initialized successfully.");

            // 2. Get a Firestore instance
            Firestore db = FirestoreClient.getFirestore();

            // 3. Prepare data to write
            Map<String, String> docData = new HashMap<>();
            docData.put("message", "Hello from Java Firebase Backend!");
            docData.put("timestamp", new java.util.Date().toString());

            // 4. Write data to Firestore
            // This will create a document named "java-hello-world" in a collection named "greetings"
            // If the collection or document does not exist, Firestore will create them.
            System.out.println("Attempting to add document to Firestore...");
            WriteResult result = db.collection("greetings").document("java-hello-world").set(docData).get();

            System.out.println("Document added successfully at: " + result.getUpdateTime());
            System.out.println("Check your Firestore database under the 'greetings' collection.");

        } catch (IOException e) {
            System.err.println("Error initializing Firebase or accessing service account key: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error writing to Firestore: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // In a real application, you might want to gracefully shut down resources
            // For this simple example, the application will exit.
        }
    }
}
