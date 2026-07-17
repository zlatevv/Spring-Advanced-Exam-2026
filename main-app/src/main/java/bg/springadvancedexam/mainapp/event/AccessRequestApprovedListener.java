package bg.springadvancedexam.mainapp.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccessRequestApprovedListener {
    @EventListener
    public void onAccessRequestApproved(AccessRequestApprovedEvent event) {
        log.info("Access request {} approved for researcher {} on manuscript {}",
                event.accessRequestId(), event.researcherId(), event.manuscriptId());
    }
}
