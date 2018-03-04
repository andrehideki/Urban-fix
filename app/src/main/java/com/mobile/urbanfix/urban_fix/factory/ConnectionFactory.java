package com.mobile.urbanfix.urban_fix.factory;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.urbanfix.urban_fix.model.User;

public final class ConnectionFactory {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    static {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled( true );
    }
    private ConnectionFactory() {}

    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth == null ) {
            firebaseAuth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if( user != null ) firebaseUser = user;
                }
            };
            firebaseAuth.addAuthStateListener( authStateListener );
        }
        return firebaseAuth;
    }

    public static DatabaseReference getDatabaseReference() {
        if(databaseReference == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled( true );
            databaseReference = firebaseDatabase.getReference();
        }
        return databaseReference;
    }

    public static DatabaseReference getUsersDatabaseReferente() {
        return getDatabaseReference().child("User").child(getFirebaseUser().getUid());
    }

    public static DatabaseReference getProblemsDatabaseReference() {
        return getDatabaseReference().child("Alerts").child(getFirebaseUser().getUid());
    }


    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static StorageReference getFirebaseStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static void logout() {
        firebaseAuth.signOut();
    }
}
