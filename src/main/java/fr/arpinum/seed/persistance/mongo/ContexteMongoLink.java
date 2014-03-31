package fr.arpinum.seed.persistance.mongo;

import fr.arpinum.seed.bus.Commande;
import fr.arpinum.seed.bus.SynchronisationBus;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;

import javax.inject.Inject;

public class ContexteMongoLink implements SynchronisationBus {


    @Inject
    public ContexteMongoLink(MongoSessionManager sessionManager) {
        sessions = ThreadLocal.withInitial(sessionManager::createSession);
    }

    @Override
    public void avantExecution(Commande<?> commande) {
        sessions.get().start();
    }

    @Override
    public void finalement() {
        sessions.get().stop();
        sessions.remove();
    }

    @Override
    public void surErreur() {
        sessions.get().clear();
    }

    public MongoSession sessionCourante() {
        return sessions.get();
    }

    private final ThreadLocal<MongoSession> sessions;

}