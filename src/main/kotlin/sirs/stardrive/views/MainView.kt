package sirs.stardrive.views

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed

@Route
@AnonymousAllowed
class MainView : VerticalLayout() {
    init {
        add(H1("Hello World!"))
    }
}