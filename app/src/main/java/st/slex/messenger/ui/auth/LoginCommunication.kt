package st.slex.messenger.ui.auth

import st.slex.messenger.ui.core.Communication

interface LoginCommunication : Communication<LoginUi> {

    class Base : Communication.Base<LoginUi>(), LoginCommunication
}