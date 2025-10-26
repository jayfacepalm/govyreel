export interface ProjectAsset {
  id: string;
  name: string;
  description: string;
}

export interface Project {
  id: string;
  name: string;
  description: string;
  createdDate: string;
  lastModifiedDate: string;
  projectAssets: ProjectAsset[];
}

export interface ProjectListType {
    projectList: Project[]
}