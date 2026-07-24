package com.finalProject.scheduler;

import com.finalProject.service.impl.PricingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceScheduler {

    private final PricingService pricingService;

    public PriceScheduler(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void updateEffectivePrices() {
        System.out.println("Scheduler running");
        pricingService.updateEffectivePrices();
        pricingService.updateEffectiveAddOnPrices();
    }
}
