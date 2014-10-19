package io.github.binaryfoo.decoders.apdu;

import io.github.binaryfoo.DecodedData;
import io.github.binaryfoo.Decoder;
import io.github.binaryfoo.HexDumpFactory;
import io.github.binaryfoo.decoders.DecodeSession;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class APDUSequenceDecoder implements Decoder {

    private ReplyAPDUDecoder replyDecoder;
    private CommandAPDUDecoder[] commandDecoders;
    private HexDumpFactory hexDumpFactory = new HexDumpFactory();

    public APDUSequenceDecoder(ReplyAPDUDecoder replyDecoder, CommandAPDUDecoder... commandDecoders) {
        this.replyDecoder = replyDecoder;
        this.commandDecoders = commandDecoders;
    }

    @Override
    public List<DecodedData> decode(@NotNull String input, int startIndexInBytes, @NotNull DecodeSession session) {
        ArrayList<DecodedData> list = new ArrayList<DecodedData>();
        for (String line : input.split("\\s+")) {
            try {
                CommandAPDUDecoder commandDecoder = getCommandDecoder(line);
                DecodedData decoded;
                if (commandDecoder != null) {
                    session.setCurrentCommand(commandDecoder.getCommand());
                    decoded = commandDecoder.decode(line, startIndexInBytes, session);
                } else {
                    decoded = replyDecoder.decode(line, startIndexInBytes, session);
                }
                decoded.setHexDump(hexDumpFactory.splitIntoByteLengthStrings(line, startIndexInBytes));
                list.add(decoded);
                startIndexInBytes = decoded.getEndIndex();
            } catch (Exception e) {
                list.add(DecodedData.primitive(line, "Failed to decode: " + e.getMessage(), 0, 0));
            }
        }
        return list;
    }

    private CommandAPDUDecoder getCommandDecoder(String input) {
        APDUCommand command = APDUCommand.fromHex(input.substring(0, 4));
        for (CommandAPDUDecoder commandDecoder : commandDecoders) {
            if (commandDecoder.getCommand() == command) {
                return commandDecoder;
            }
        }
        return null;
    }

    @Override
    public String validate(String input) {
        return null;
    }

    @Override
    public int getMaxLength() {
        return Integer.MAX_VALUE;
    }
}
