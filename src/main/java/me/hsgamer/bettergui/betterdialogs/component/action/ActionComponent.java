package me.hsgamer.bettergui.betterdialogs.component.action;

import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.dialog.*;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class ActionComponent extends DialogComponent {
    private final String label;
    private final @Nullable String tooltip;
    private final int width;
    private final @Nullable String assign;

    protected ActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        label = Optional.ofNullable(input.options().get("label"))
                .map(Object::toString)
                .orElse("Action");
        tooltip = Optional.ofNullable(input.options().get("tooltip"))
                .map(Object::toString)
                .orElse(null);
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(150);
        assign = Optional.ofNullable(input.options().get("assign"))
                .map(Object::toString)
                .orElse(null);
    }

    protected abstract void getAction(Player player, PEDialogActionBuilder builder);

    @Override
    public void apply(Player player, PEDialog<?> dialog) {
        Consumer<PEDialogActionBuilder> actionConsumer = builder -> {
            builder
                    .label(StringReplacerApplier.replace(label, player.getUniqueId(), this))
                    .tooltip(tooltip == null ? null : StringReplacerApplier.replace(tooltip, player.getUniqueId(), this))
                    .width(width);
            getAction(player, builder);
        };
        switch (dialog) {
            case PEConfirmationDialog confirmationDialog -> {
                if (assign == null) {
                    if (!confirmationDialog.hasYesAction()) {
                        confirmationDialog.yesAction(actionConsumer);
                    } else {
                        confirmationDialog.noAction(actionConsumer);
                    }
                } else if (assign.equalsIgnoreCase("yes")) {
                    confirmationDialog.yesAction(actionConsumer);
                } else if (assign.equalsIgnoreCase("no")) {
                    confirmationDialog.noAction(actionConsumer);
                }
            }
            case PEMultiActionDialog multiActionDialog -> {
                if (assign == null || !assign.equalsIgnoreCase("exit")) {
                    multiActionDialog.action(actionConsumer);
                } else {
                    multiActionDialog.exitAction(actionConsumer);
                }
            }
            case PENoticeDialog noticeDialog -> noticeDialog.action(actionConsumer);
            case PEServerLinksDialog serverLinksDialog -> serverLinksDialog.exitAction(actionConsumer);
            case null, default -> {
            }
        }
    }
}
