/*
   Copyright 2025-2026 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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
