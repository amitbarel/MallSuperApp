package superapp.logic;

import java.util.List;

import superapp.restApi.boundaries.MiniAppCommandBoundary;

public interface MiniAppCommandsQueries extends MiniAppCommandsWithASyncSupport {

	public List<MiniAppCommandBoundary> getAllMiniAppsCommandsAdminOnly(String superapp, String email, int size, int page);

	public List<MiniAppCommandBoundary> getSpecificMiniAppCommandsAdminOnly(String miniAppName, String superapp,
			String email, int size, int page);
	
	public void deleteAllCommandsAdminOnly(String superapp, String email);
}