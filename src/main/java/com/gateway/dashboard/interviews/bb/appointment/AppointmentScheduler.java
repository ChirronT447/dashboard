package com.gateway.dashboard.interviews.bb.appointment;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

record Appointment(LocalDateTime start, LocalDateTime end, String subject){}

public class AppointmentScheduler {

    private final List<Appointment> bookedAppointments = new ArrayList<>();

    // Define standard working hours for calculating free slots.
    private static final LocalTime WORK_DAY_START = LocalTime.of(9, 0);
    private static final LocalTime WORK_DAY_END = LocalTime.of(17, 0);
    private static final Duration SLOT_DURATION = Duration.ofMinutes(60);

    /**
     * Books a new appointment if it doesn't conflict with existing ones.
     * This method corresponds to the 'ShouldBookAppointment' and 'ShouldDetectConflicts' requirements.
     *
     * @param start The start time of the new appointment.
     * @param end The end time of the new appointment.
     * @return true if the appointment was successfully booked, false if it conflicted with an existing one.
     * @throws IllegalArgumentException if the end time is not after the start time.
     */
    public boolean bookAppointment(LocalDateTime start, LocalDateTime end, String subject) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Appointment end time must be after the start time.");
        }

        Appointment newAppointment = new Appointment(start, end, subject);

        if (hasConflict(newAppointment)) {
            return false; // Rejects overlapping appointments
        }

        bookedAppointments.add(newAppointment);
        // Keep the list sorted by start time for efficient lookups.
        bookedAppointments.sort(Comparator.comparing(Appointment::start));
        return true;
    }

    /**
     * Checks if a new appointment overlaps with any existing appointments.
     * An overlap occurs if start1 < end2 AND start2 < end1.
     */
    private boolean hasConflict(Appointment newAppointment) {
        return bookedAppointments.stream()
                .anyMatch(existing -> newAppointment.start().isBefore(existing.end()) &&
                        newAppointment.end().isAfter(existing.start()));
    }

    /**
     * Calculates all available 60-minute slots for a given day within working hours.
     * This method corresponds to the 'ShouldReturnFreeSlots' requirement.
     *
     * @param day The specific day to find free slots for.
     * @return A list of available appointments representing free 60-minute slots.
     */
    public List<Appointment> getAvailableSlots(LocalDate day) {
        List<Appointment> freeSlots = new ArrayList<>();
        LocalDateTime slotStart = day.atTime(WORK_DAY_START);
        LocalDateTime dayEnd = day.atTime(WORK_DAY_END);

        // Get appointments that are relevant for the given day.
        List<Appointment> appointmentsForDay = bookedAppointments.stream()
                .filter(a -> a.start().toLocalDate().equals(day))
                .toList();

        // Iterate through the gaps before each appointment.
        for (Appointment booked : appointmentsForDay) {
            findSlotsInGap(slotStart, booked.start(), freeSlots);
            slotStart = booked.end();
        }

        // Find slots in the final gap between the last appointment and the end of the day.
        findSlotsInGap(slotStart, dayEnd, freeSlots);

        return freeSlots;
    }

    /**
     * Helper method to find all possible slots within a given time interval.
     */
    private void findSlotsInGap(LocalDateTime gapStart, LocalDateTime gapEnd, List<Appointment> freeSlots) {
        LocalDateTime currentSlot = gapStart;
        while (!currentSlot.plus(SLOT_DURATION).isAfter(gapEnd)) {
            LocalDateTime slotEnd = currentSlot.plus(SLOT_DURATION);
            freeSlots.add(new Appointment(currentSlot, slotEnd, ""));
            currentSlot = currentSlot.plus(SLOT_DURATION);
        }
    }

}
