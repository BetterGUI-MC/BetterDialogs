package me.hsgamer.bettergui.betterdialogs.requirement;

import io.github.projectunified.unidialog.viaversion.ViaVersionDialogManager;
import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;

import java.util.Objects;
import java.util.UUID;

public class DialogSupportRequirement extends BaseRequirement<Boolean> {
    public DialogSupportRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    @Override
    protected Boolean convert(Object value, UUID uuid) {
        return Boolean.parseBoolean(Objects.toString(value));
    }

    @Override
    protected Result checkConverted(UUID uuid, Boolean value) {
        return Objects.equals(value, ViaVersionDialogManager.supportsDialog(uuid)) ? Result.success() : Result.fail();
    }
}
