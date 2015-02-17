package net.younguard.bighorn.broadcast.adapter;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.broadcast.adapter.broadcast.MsgPingAdapter;
import net.younguard.bighorn.broadcast.adapter.broadcast.QueryOnlineNumAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameHistoryQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameInviteCreateAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameInviteQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameJoinAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameLoadManualAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GamePlayStepAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GamePlayingQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameResignAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.GameSyncStepAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.MyHistoryQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.MyInviteQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.MyPlayingQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.PlayerHistoryQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.PlayerInviteQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.chess.PlayerPlayingQueryPaginationAdapter;
import net.younguard.bighorn.broadcast.adapter.session.AccountInfoModifyAdapter;
import net.younguard.bighorn.broadcast.adapter.session.DeviceLoginAdapter;
import net.younguard.bighorn.broadcast.adapter.session.RegisterDeviceNotifyTokenAdapter;
import net.younguard.bighorn.broadcast.adapter.session.SocketCloseAdapter;
import net.younguard.bighorn.comm.Command;
import net.younguard.bighorn.comm.CommandParser;
import net.younguard.bighorn.comm.tlv.TlvObject;

/**
 * command parser.
 * 
 * Copyright 2014-2015 by Young Guard Salon Community, China. All rights
 * reserved. http://www.younguard.net
 * 
 * NOTICE ! You can copy or redistribute this code freely, but you should not
 * remove the information about the copyright notice and the author.
 * 
 * @author ThomasZhang, thomas.zh@qq.com
 */
public class BroadAdapterParser
		extends CommandParser
{
	public static TlvObject encode(Command cmd)
			throws UnsupportedEncodingException
	{
		return cmd.encode();
	}

	public static Command decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		switch (tlv.getTag()) {
		case CommandTag.ACCOUNT_INFO_MODIFY_REQUEST:
			return new AccountInfoModifyAdapter().decode(tlv);
		case CommandTag.DEVICE_LOGIN_REQUEST:
			return new DeviceLoginAdapter().decode(tlv);
		case CommandTag.REGISTER_NOTIFY_TOKEN_REQUEST:
			return new RegisterDeviceNotifyTokenAdapter().decode(tlv);
		case CommandTag.SOCKET_CLOSE_REQUEST:
			return new SocketCloseAdapter().decode(tlv);

		case CommandTag.GAME_HISTORY_QUERY_PAGINATION_REQUEST:
			return new GameHistoryQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_INVITE_CREATE_REQUEST:
			return new GameInviteCreateAdapter().decode(tlv);
		case CommandTag.GAME_INVITE_QUERY_PAGINATION_REQUEST:
			return new GameInviteQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_JOIN_REQUEST:
			return new GameJoinAdapter().decode(tlv);
		case CommandTag.GAME_LOAD_MANUAL_REQUEST:
			return new GameLoadManualAdapter().decode(tlv);
		case CommandTag.GAME_PLAYING_QUERY_PAGINATION_REQUEST:
			return new GamePlayingQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_MY_HISTORY_QUERY_PAGINATION_REQUEST:
			return new MyHistoryQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_MY_INVITE_QUERY_PAGINATION_REQUEST:
			return new MyInviteQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_MY_PLAYING_QUERY_PAGINATION_REQUEST:
			return new MyPlayingQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_PLAY_STEP_REQUEST:
			return new GamePlayStepAdapter().decode(tlv);
		case CommandTag.GAME_PLAYER_HISTORY_QUERY_PAGINATION_REQUEST:
			return new PlayerHistoryQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_PLAYER_INVITE_QUERY_PAGINATION_REQUEST:
			return new PlayerInviteQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_PLAYER_PLAYING_QUERY_PAGINATION_REQUEST:
			return new PlayerPlayingQueryPaginationAdapter().decode(tlv);
		case CommandTag.GAME_RESIGN_REQUEST:
			return new GameResignAdapter().decode(tlv);
		case CommandTag.GAME_SYNC_STEP_REQUEST:
			return new GameSyncStepAdapter().decode(tlv);

		case CommandTag.MESSAGE_PING_REQUEST:
			return new MsgPingAdapter().decode(tlv);
		case CommandTag.QUERY_ONLINE_NUMBER_REQUEST:
			return new QueryOnlineNumAdapter().decode(tlv);

		default:
			throw new UnsupportedEncodingException("Unknown command=[" + tlv.getTag() + "]");
		}
	}
}
