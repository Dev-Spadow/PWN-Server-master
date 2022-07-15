package com.arlania.world.content.dailytasks;

import com.arlania.engine.task.Task;

import java.time.LocalDateTime;

final class DailyUpdateTask extends Task {

    DailyUpdateTask() {
        super(1000, true);
    }

    @Override
    protected void execute() {
        final LocalDateTime date = LocalDateTime.now();
        if (DailyTasks.INSTANCE.getLastUpdate() == null || date.getHour() == 0 && date.getDayOfMonth() != DailyTasks.INSTANCE.getLastUpdate().getDayOfMonth()) {
            DailyTasks.INSTANCE.setLastUpdate(date);
            DailyTasks.INSTANCE.updateDailyTasks();
        }
    }
}
