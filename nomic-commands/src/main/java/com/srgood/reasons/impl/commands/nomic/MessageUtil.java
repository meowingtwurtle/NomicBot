package com.srgood.reasons.impl.commands.nomic;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.util.StringUtils;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.srgood.reasons.impl.commands.nomic.JDAUtil.getProposalsChannel;
import static com.srgood.reasons.impl.commands.nomic.JDAUtil.getRulesChannel;

public class MessageUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMMM d h:mm a 'UTC'");

    public static Rule displayRule(Rule rule) {
        Message formattedMessage = formatRule(rule);
        if (rule.getMessageID() != 0) {
            try {
                getRulesChannel().editMessageById(rule.getMessageID(), formattedMessage).complete();
                return rule;
            } catch (ErrorResponseException e) {
                // If message deleted, resend with existing code
                // Otherwise, error will be resent when it happens again below
            }
        }
        Message ruleMessage = getRulesChannel().sendMessage(formattedMessage).complete();
        Rule updatedRule = rule.withMessageID(ruleMessage.getIdLong());
        DBUtil.writeRule(updatedRule);
        return updatedRule;
    }

    private static Message formatRule(Rule rule) {
        EmbedBuilder builder = new EmbedBuilder();
        Proposal proposal = rule.getProposal();

        String history = getProposalHistory(proposal).stream().collect(Collectors.joining("\n"));

        builder.setTitle(getRuleName(proposal) + " (" + rule.getMutability().toString().toLowerCase() + ")");
        builder.setColor(Color.GREEN);

        switch (proposal.getType()) {
            case TRANSMUTATION_MUTABLE_TO_IMMUTABLE:
            case TRANSMUTATION_IMMUTABLE_TO_MUTABLE:
                builder.setDescription(DBUtil.getProposalByID(proposal.getAffectedRule()).getText());
                break;
            default:
                builder.setDescription(proposal.getText());
                break;
        }

        builder.addField("History", history, true);

        return new MessageBuilder().setEmbed(builder.build()).build();
    }

    private static String getRuleName(Proposal proposal) {
        return (proposal.getType() == ProposalType.INITIAL_RULE ? "Initial Set " : "Rule ") + proposal.getNumber();
    }

    private static String getRuleName(int number) {
        return getRuleName(DBUtil.getProposalByID(number));
    }

    private static List<String> getProposalHistory(Proposal proposal) {
        List<String> ret = new ArrayList<>();
        switch (proposal.getType()) {
            case INITIAL_RULE:
            case ENACTMENT:
                ret.add(0, getRuleName(proposal) + " enacted.");
                break;
            case AMENDMENT:
                ret.add(0, getRuleName(proposal.getAffectedRule()) + " amended by " + getRuleName(proposal) + ".");
                ret.addAll(0, getProposalHistory(proposal.getAffectedRule()));
                break;
            case TRANSMUTATION_IMMUTABLE_TO_MUTABLE:
                ret.add(0, getRuleName(proposal.getAffectedRule()) + " transmuted from immutable to mutable by " + getRuleName(proposal) + ".");
                ret.addAll(0, getProposalHistory(proposal.getAffectedRule()));
                break;
            case TRANSMUTATION_MUTABLE_TO_IMMUTABLE:
                ret.add(0, getRuleName(proposal.getAffectedRule()) + " transmuted from mutable to immutable by " + getRuleName(proposal) + ".");
                ret.addAll(0, getProposalHistory(proposal.getAffectedRule()));
                break;
        }
        return ret;
    }

    private static List<String> getProposalHistory(int number) {
        return getProposalHistory(DBUtil.getProposalByID(number));
    }

    public static Proposal displayProposal(Proposal proposal) {
        Message formattedMessage = formatProposal(proposal);
        if (proposal.getMessageID() != 0) {
            try {
                getProposalsChannel().editMessageById(proposal.getMessageID(), formattedMessage).complete();
                addProposalReactions(proposal);
                return proposal;
            } catch (ErrorResponseException ignored) {
            } // Message is most likely gone, so drop exception. If it isn't, we'll get it again later.
        }
        Message newMessage = getProposalsChannel().sendMessage(formattedMessage).complete();
        Proposal updatedProp = proposal.withMessageID(newMessage.getIdLong());
        DBUtil.writeProposal(updatedProp);
        addProposalReactions(updatedProp);
        return updatedProp;
    }

    private static void addProposalReactions(Proposal proposal) {
        if (proposal.getState() == ProposalState.VOTING) {
            getProposalsChannel().addReactionById(proposal.getMessageID(), "\u2611").complete();
            getProposalsChannel().addReactionById(proposal.getMessageID(), "\uD83C\uDDFD").complete();
        } else {
            getProposalsChannel().getMessageById(proposal.getMessageID())
                                 .complete()
                                 .getReactions()
                                 .stream()
                                 .filter(reaction -> List.of("\u2611", "\uD83C\uDDFD")
                                                         .contains(reaction.getEmote().getName()))
                                 .forEach(messageReaction -> messageReaction.removeReaction().complete());
        }
    }

    private static Message formatProposal(Proposal proposal) {
        if (proposal.getType() != ProposalType.INITIAL_RULE) {
            EmbedBuilder builder = formatStandardProposal(proposal);
            return new MessageBuilder().setEmbed(builder.build()).build();
        } else {
            return formatInitialRuleProposal(proposal);
        }
    }

    private static EmbedBuilder formatStandardProposal(Proposal proposal) {
        EmbedBuilder builder = new EmbedBuilder();
        formatProposalEmbedDescription(proposal, builder);
        formatProposalStatusEmbedFields(proposal, builder);
        formatProposalVoteEmbedFields(proposal, builder);
        return builder;
    }

    private static void formatProposalEmbedDescription(Proposal proposal, EmbedBuilder builder) {
        Pair<String, Boolean> extraInfo = getProposalInfo(proposal);

        builder.setTitle("Proposal " + proposal.getNumber() + ": " + extraInfo.getLeft());
        if (extraInfo.getRight()) {
            builder.setDescription(proposal.getText());
        }
    }

    private static void formatProposalStatusEmbedFields(Proposal proposal, EmbedBuilder builder) {
        switch (proposal.getState()) {
            case VOTING:
                builder.setColor(Color.GRAY);
                builder.addField("Status", "Voting is open.", true);
                builder.addField("Vote Required", StringUtils.capitalize(proposal.getType().getVotesRequired().readableName().toLowerCase()), true);
                builder.addField("Vote Closes", DATE_TIME_FORMATTER.format(((ProposalVoting) proposal).voteExpiry()
                                                                                                  .toInstant()
                                                                                                  .truncatedTo(ChronoUnit.MINUTES)
                                                                                                  .atZone(ZoneId.of("UTC"))), true);
                break;
            case ENACTED:
                builder.setColor(Color.GREEN);
                builder.addField("Status", "Adopted.", false);
                break;
            case DEFEATED:
                builder.setColor(Color.RED);
                builder.addField("Status", "Defeated.", false);
                break;
        }
    }

    private static void formatProposalVoteEmbedFields(Proposal proposal, EmbedBuilder builder) {
        builder.addField("Yea Votes", "Count: " + proposal.getYeaVoteCount(), true);
        builder.addField("Nay Votes", "Count: " + proposal.getNayVoteCount(), true);
    }

    private static Message formatInitialRuleProposal(Proposal proposal) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Initial Set " + proposal.getNumber());
        builder.setColor(Color.GREEN);
        builder.setDescription(proposal.getText());
        return new MessageBuilder().setEmbed(builder.build()).build();
    }

    private static Pair<String, Boolean> getProposalInfo(Proposal proposal) {
        String extraInfo = "";
        boolean hasDescription = false;
        switch (proposal.getType()) {
            case ENACTMENT:
                extraInfo = String.format("to add a Rule %d with the following text:", proposal.getNumber());
                hasDescription = true;
                break;
            case AMENDMENT:
                extraInfo = "to amend " + getRuleName(proposal.getAffectedRule()) + " to the following:";
                hasDescription = true;
                break;
            case REPEAL:
                extraInfo = "to repeal " + getRuleName(proposal.getAffectedRule());
                hasDescription = false;
                break;
            case TRANSMUTATION_MUTABLE_TO_IMMUTABLE:
                extraInfo = "to transmute " + getRuleName(proposal.getAffectedRule()) + " to be immutable";
                hasDescription = false;
                break;
            case TRANSMUTATION_IMMUTABLE_TO_MUTABLE:
                extraInfo = "to transmute " + getRuleName(proposal.getAffectedRule()) + " to be mutable";
                hasDescription = false;
                break;
        }
        return Pair.of(extraInfo, hasDescription);
    }

}
