package org.example.stamppaw_backend.badge.service;

import org.example.stamppaw_backend.badge.entity.Badge;

public interface BadgeEvaluator {

    boolean evaluate(Badge badge, Long userId);

    int calculateProgress(Badge badge, Long userId);
}

