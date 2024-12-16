package superapp.logic;

import superapp.restApi.boundaries.MiniAppCommandBoundary;

public interface MiniAppCommandsWithASyncSupport extends MiniAppCommandsService {
	
	public MiniAppCommandBoundary invokeCommandWithAsyncOption(MiniAppCommandBoundary miniAppCommandBoundary,boolean isAsync);

}
