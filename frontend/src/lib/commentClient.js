const { default: httpClient } = require("./httpClient");


const getComments = (versionId) => httpClient.get(`/api/files/${versionId}/comments`);
const addComment = (versionId,data) => httpClient.post(`/api/files/${versionId}/comments`,data);

const commentClient = {
    getComments,
    addComment,
  };

export default commentClient;
