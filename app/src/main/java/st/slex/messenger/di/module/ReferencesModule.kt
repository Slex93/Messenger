package st.slex.messenger.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module
class ReferencesModule {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesDataBaseReference(): DatabaseReference = FirebaseDatabase.getInstance().reference
}