package net.younguard.bighorn.broadcast.adapter.chess;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.ErrorCode;
import net.younguard.bighorn.broadcast.service.GameService;
import net.younguard.bighorn.broadcast.util.BighornApplicationContextUtil;
import net.younguard.bighorn.chess.cmd.PlayerPlayingQueryPaginationReq;
import net.younguard.bighorn.chess.cmd.PlayerPlayingQueryPaginationResp;
import net.younguard.bighorn.comm.RequestCommand;
import net.younguard.bighorn.comm.ResponseCommand;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.LogErrorMessage;
import net.younguard.bighorn.domain.GameMasterInfo;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerPlayingQueryPaginationAdapter
		extends RequestCommand
{
	public PlayerPlayingQueryPaginationAdapter()
	{
		super();

		this.setTag(CommandTag.GAME_PLAYER_PLAYING_QUERY_PAGINATION_REQUEST);
	}

	@Override
	public RequestCommand decode(TlvObject tlv)
			throws UnsupportedEncodingException
	{
		reqCmd = (PlayerPlayingQueryPaginationReq) new PlayerPlayingQueryPaginationReq().decode(tlv);
		this.setSequence(reqCmd.getSequence());

		return this;
	}

	@Override
	public ResponseCommand execute(IoSession session)
			throws Exception
	{
		short pageNum = reqCmd.getPageNum();
		short pageSize = reqCmd.getPageSize();
		String accountId = reqCmd.getPlayerId();
		String deviceId = (String) session.getAttribute("deviceId");

		try {
			GameService gameService = BighornApplicationContextUtil.getGameService();

			List<GameMasterInfo> games = gameService.queryPlayingPagination(pageNum, pageSize, accountId);
			logger.info("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.SUCCESS + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|");

			PlayerPlayingQueryPaginationResp respCmd = new PlayerPlayingQueryPaginationResp(this.getSequence(),
					ErrorCode.SUCCESS, games);
			return respCmd;
		} catch (Exception e) {
			logger.warn("commandTag=[" + this.getTag() + "]|ErrorCode=[" + ErrorCode.UNKNOWN_FAILURE + "]|sessionId=["
					+ session.getId() + "]|device=[" + deviceId + "]|" + LogErrorMessage.getFullInfo(e));

			PlayerPlayingQueryPaginationResp respCmd = new PlayerPlayingQueryPaginationResp(this.getSequence(),
					ErrorCode.UNKNOWN_FAILURE);
			return respCmd;
		}
	}

	private PlayerPlayingQueryPaginationReq reqCmd;

	private final static Logger logger = LoggerFactory.getLogger(PlayerPlayingQueryPaginationAdapter.class);

}
