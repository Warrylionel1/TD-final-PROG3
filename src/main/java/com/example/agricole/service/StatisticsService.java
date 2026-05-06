package com.example.agricole.service;

import com.example.agricole.dto.*;
import com.example.agricole.entity.Collectivity;
import com.example.agricole.enums.Frequency;
import com.example.agricole.repository.CollectivityRepository;
import com.example.agricole.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StatisticsService {

    private final CollectivityRepository collectivityRepository;
    private final StatisticsRepository repository;

    public StatisticsService(CollectivityRepository collectivityRepository, StatisticsRepository repository) {
        this.collectivityRepository = collectivityRepository;
        this.repository = repository;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(
            String collectivityId,
            LocalDate from,
            LocalDate to) {

        List<MemberDescription> members =
                repository.getMembers(collectivityId);

        Map<String, Double> payments =
                repository.getPaymentsByMember(collectivityId, from, to);

        List<MembershipFeeRaw> fees =
                repository.getActiveMembershipFees(collectivityId);

        double expected = calculateExpected(fees, from, to);

        List<CollectivityLocalStatistics> result = new ArrayList<>();

        for (MemberDescription member : members) {

            double earned = payments.getOrDefault(member.getId(), 0.0);
            double unpaid = Math.max(expected - earned, 0);

            result.add(new CollectivityLocalStatistics(
                    member,
                    earned,
                    unpaid
            ));
        }

        return result;
    }

    public List<CollectivityOverallStatistics> getOverallStatistics(
            LocalDate from,
            LocalDate to) {

        List<String> collectivityIds =
                collectivityRepository.getAllCollectivityIds();

        List<CollectivityOverallStatistics> result = new ArrayList<>();

        for (String id : collectivityIds) {

            List<MemberDescription> members =
                    repository.getMembers(id);

            Map<String, Double> payments =
                    repository.getPaymentsByMember(id, from, to);

            List<MembershipFeeRaw> fees =
                    repository.getActiveMembershipFees(id);

            long newMembers =
                    repository.countNewMembers(id, from, to);

            long upToDate = 0;

            for (MemberDescription m : members) {

                double paid = payments.getOrDefault(m.getId(), 0.0);

                boolean ok = true;

                for (MembershipFeeRaw fee : fees) {

                    long periods = switch (Frequency.valueOf(fee.getFrequency())) {
                        case WEEKLY -> ChronoUnit.WEEKS.between(from, to);
                        case MONTHLY -> ChronoUnit.MONTHS.between(from, to);
                        case ANNUALLY -> ChronoUnit.YEARS.between(from, to);
                        case PUNCTUALLY -> 1;
                    };

                    if (periods < 1) periods = 1;

                    double expected = periods * fee.getAmount();

                    if (paid < expected) {
                        ok = false;
                        break;
                    }
                }

                if (ok) {
                    upToDate++;
                }
            }

            double percentage = members.isEmpty()
                    ? 0
                    : (upToDate * 100.0) / members.size();

            CollectivityOverallStatistics stats =
                    new CollectivityOverallStatistics();

            Collectivity c = collectivityRepository.findById(id);

            if (c != null) {
                CollectivityInformation info = new CollectivityInformation();
                info.setName(c.getName());
                info.setNumber(c.getNumber());
                stats.setCollectivityInformation(info);
            }

            stats.setNewMembersNumber((int) newMembers);
            stats.setOverallMemberCurrentDuePercentage(percentage);

            result.add(stats);
        }

        return result;
    }

    private double calculateExpected(
            List<MembershipFeeRaw> fees,
            LocalDate from,
            LocalDate to) {

        double total = 0;

        for (MembershipFeeRaw fee : fees) {

            long periods = switch (Frequency.valueOf(fee.getFrequency())) {
                case WEEKLY -> ChronoUnit.WEEKS.between(from, to);
                case MONTHLY -> ChronoUnit.MONTHS.between(from, to);
                case ANNUALLY -> ChronoUnit.YEARS.between(from, to);
                case PUNCTUALLY -> 1;
            };

            if (periods < 1) periods = 1;

            total += periods * fee.getAmount();
        }

        return total;
    }
}