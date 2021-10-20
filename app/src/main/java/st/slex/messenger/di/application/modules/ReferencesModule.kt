package st.slex.messenger.di.application.modules

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides

@Module
class ReferencesModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseUser = Firebase.auth.currentUser!!

    @Provides
    fun provideDataBaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference

    @Provides
    fun provideStorageReference(): StorageReference = FirebaseStorage.getInstance().reference
}