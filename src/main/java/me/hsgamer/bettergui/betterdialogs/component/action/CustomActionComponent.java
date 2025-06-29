package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.DynamicCustomAction;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import me.hsgamer.bettergui.action.ActionApplier;
import me.hsgamer.bettergui.api.requirement.Requirement;
import me.hsgamer.bettergui.betterdialogs.builder.ActionComponentBuilder;
import me.hsgamer.bettergui.requirement.RequirementApplier;
import me.hsgamer.bettergui.util.ProcessApplierConstants;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.task.BatchRunnable;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class CustomActionComponent extends ActionComponent {
    private final ResourceLocation actionId;

    public CustomActionComponent(ActionComponentBuilder.Input input) {
        super(input);
        ActionApplier actionApplier = Optional.ofNullable(MapUtils.getIfFound(input.options(), "action", "command"))
                .map(o -> new ActionApplier(getMenu(), o))
                .orElse(ActionApplier.EMPTY);
        RequirementApplier clickRequirementApplier = Optional.ofNullable(input.options().get("click-requirement"))
                .flatMap(MapUtils::castOptionalStringObjectMap)
                .map(m -> new RequirementApplier(getMenu(), input.buttonComponent().getName() + "_click", m))
                .orElse(RequirementApplier.EMPTY);
        this.actionId = input.buttonComponent().getMenu().registerAction(input.buttonComponent().getName(), player -> {
            UUID uuid = player.getUniqueId();
            BatchRunnable batchRunnable = new BatchRunnable();
            batchRunnable.getTaskPool(ProcessApplierConstants.REQUIREMENT_ACTION_STAGE)
                    .addLast(process -> {
                        Requirement.Result result = clickRequirementApplier.getResult(uuid);
                        result.applier.accept(uuid, process);
                        if (result.isSuccess) {
                            process.getTaskPool(ProcessApplierConstants.ACTION_STAGE).addLast(actionProcess -> actionApplier.accept(uuid, actionProcess));
                        }
                        process.next();
                    });
            SchedulerUtil.async().run(batchRunnable);
        });
    }

    @Override
    public Action getAction(Player player) {
        return new DynamicCustomAction(actionId, null);
    }
}
