// =====================================================
// Project: bv-admin
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.bv_admin.domain.mailversand.process;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;

/**
 * MailversandauftragScheduler scheduled die Abarbeitung eines
 * Mailversandauftrags.
 */
@ApplicationScoped
public class MailversandauftragScheduler {

    @Inject
    MailversandProcessor processor;

    @Scheduled(
            cron = "{mailversand.cron.expr}",
            concurrentExecution = ConcurrentExecution.SKIP,
            identity = "mailversand")
    void startJob() {

        this.processor.processMailversandauftrag();
    }

}
