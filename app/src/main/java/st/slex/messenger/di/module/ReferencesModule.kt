package st.slex.messenger.di.module

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class ReferencesModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseUser = Firebase.auth.currentUser!!

    @Provides
    fun providesDataBaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference
}