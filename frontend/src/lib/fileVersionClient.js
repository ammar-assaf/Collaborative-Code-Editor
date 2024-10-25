const { default: httpClient } = require("./httpClient");


const forkFile = (fileId) => httpClient.post(`api/files/${fileId}/versions/fork`);
const getFileVersions = (fileId) => httpClient.get(`api/files/${fileId}/versions`);
const getVersionContent =  (fileId,versionId) => httpClient.get(`api/files/${fileId}/versions/${versionId}/content`);
const getVersion = (fileId,versionId) => httpClient.get(`api/files/${fileId}/versions/${versionId}`);

const fileVersionClient = {
        forkFile,
        getFileVersions,
        getVersionContent,
        getVersion,

  };

export default fileVersionClient;
