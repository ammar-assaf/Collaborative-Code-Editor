const { default: httpClient } = require("./httpClient");

const getFileContent = (id) => httpClient.get(`/api/files/${id}/content`);
const getFileById = (id) => httpClient.get(`/api/files/${id}`);
const deleteFileById = (id) => httpClient.delete(`/api/files/${id}`);
const updateFileContent = (fileId, content) => httpClient.put(`/api/files/${fileId}/content`, content);
const getUserFiles = () => httpClient.get("/api/files");
const createFile = (filename, language) => httpClient.post(`/api/files?filename=${encodeURIComponent(filename)}&language=${encodeURIComponent(language)}`);
const addUserPermission=(id,fileToShare) =>httpClient.post(`/api/files/add-permission/${id}`,fileToShare);


    const fileClient = {
      getFileContent,
      getFileById,
      updateFileContent,
      getUserFiles,
      createFile,
      addUserPermission,
      deleteFileById,
  };

export default fileClient;
