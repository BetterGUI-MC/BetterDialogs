package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.ConfirmationDialog;
import com.github.retrooper.packetevents.protocol.dialog.DialogAction;
import com.github.retrooper.packetevents.protocol.dialog.action.DynamicCustomAction;
import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.body.ItemDialogBody;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import com.github.retrooper.packetevents.protocol.dialog.input.Input;
import com.github.retrooper.packetevents.protocol.dialog.input.NumberRangeInputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.TextInputControl;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class TestDialog {
    public static ConfirmationDialog createTestDialog() {
        Component title = Component.text("Test Dialog");
        Component externalTitle = Component.text("External Title");
        List<DialogBody> dialogBodies = new ArrayList<>();
        dialogBodies.add(new PlainMessageDialogBody(
                new PlainMessage(
                        LegacyComponentSerializer.legacyAmpersand().deserialize("This is a test dialog body with &aGreen text and &cRed text."),
                        50
                )
        ));
        dialogBodies.add(new PlainMessageDialogBody(
                new PlainMessage(
                        LegacyComponentSerializer.legacyAmpersand().deserialize("This is another body with &bBlue text."),
                        20
                )
        ));
        dialogBodies.add(new PlainMessageDialogBody(
                new PlainMessage(
                        LegacyComponentSerializer.legacyAmpersand().deserialize("This is a third body with &eYellow text."),
                        10
                )
        ));
        dialogBodies.add(new ItemDialogBody(
                new ItemStack.Builder()
                        .type(ItemTypes.STONE)
                        .amount(1)
                        .component(ComponentTypes.ITEM_NAME, LegacyComponentSerializer.legacyAmpersand().deserialize("&6Stone Item"))
                        .component(ComponentTypes.LORE, new ItemLore(
                                List.of(
                                        LegacyComponentSerializer.legacyAmpersand().deserialize("&7This is a stone item."),
                                        LegacyComponentSerializer.legacyAmpersand().deserialize("&aIt has &bmultiple &clines of lore.")
                                )
                        ))
                        .build(),
                new PlainMessage(
                        LegacyComponentSerializer.legacyAmpersand().deserialize("This is an item dialog body with &6Golden text."),
                        30
                ),
                true,
                true,
                20,
                30
        ));

        List<Input> dialogInputs = new ArrayList<>();
        dialogInputs.add(
                new Input(
                        "input1",
                        new TextInputControl(
                                100,
                                LegacyComponentSerializer.legacyAmpersand().deserialize("Enter your name:"),
                                true,
                                "Test User",
                                200,
                                new TextInputControl.MultilineOptions(1, 10)
                        )
                )
        );
        dialogInputs.add(
                new Input(
                        "input2",
                        new NumberRangeInputControl(
                                50,
                                LegacyComponentSerializer.legacyAmpersand().deserialize("Enter a number between 1 and 100:"),
                                "options.generic_value",
                                new NumberRangeInputControl.RangeInfo(
                                        1,
                                        100,
                                        1F,
                                        1F
                                )
                        )
                )
        );

        NBTCompound additions = new NBTCompound();
        additions.setTag("TestKey", new NBTString("TestValue"));

        return new ConfirmationDialog(
                new CommonDialogData(
                        title,
                        externalTitle,
                        true,
                        false,
                        DialogAction.CLOSE,
                        dialogBodies,
                        dialogInputs
                ),
                new ActionButton(
                        new CommonButtonData(
                                Component.text("Confirm"),
                                Component.text("Click to confirm the dialog"),
                                50
                        ),
                        new DynamicCustomAction(
                                new ResourceLocation("betterdialogs", "confirm_action"),
                                additions
                        )
                ),
                new ActionButton(
                        new CommonButtonData(
                                Component.text("Cancel"),
                                Component.text("Click to cancel the dialog"),
                                50
                        ),
                        new DynamicCustomAction(
                                new ResourceLocation("betterdialogs", "cancel_action"),
                                additions
                        )
                )
        );
    }
}
