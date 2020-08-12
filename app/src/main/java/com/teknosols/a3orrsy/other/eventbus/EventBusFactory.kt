package com.teknosols.a3orrsy.other.eventbus

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus

import java.util.concurrent.Executors

public class EventBusFactory {

    companion object {
        private val eventBus = AsyncEventBus(Executors.newCachedThreadPool())

        public fun getEventBus(): EventBus {
            return eventBus
        }
    }

}