package sirs.stardrive.views

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import org.springframework.core.env.Environment

@Route
@AnonymousAllowed
class MainView(environment: Environment) : VerticalLayout() {
    init {
        add(H1("Hello World!"))
        environment.activeProfiles.forEach { add(H2(it)) }
    }
}